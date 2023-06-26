import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFinishedPayment } from '../finished-payment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../finished-payment.test-samples';

import { FinishedPaymentService } from './finished-payment.service';

const requireRestSample: IFinishedPayment = {
  ...sampleWithRequiredData,
};

describe('FinishedPayment Service', () => {
  let service: FinishedPaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IFinishedPayment | IFinishedPayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FinishedPaymentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a FinishedPayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const finishedPayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(finishedPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinishedPayment', () => {
      const finishedPayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(finishedPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinishedPayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinishedPayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FinishedPayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFinishedPaymentToCollectionIfMissing', () => {
      it('should add a FinishedPayment to an empty array', () => {
        const finishedPayment: IFinishedPayment = sampleWithRequiredData;
        expectedResult = service.addFinishedPaymentToCollectionIfMissing([], finishedPayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finishedPayment);
      });

      it('should not add a FinishedPayment to an array that contains it', () => {
        const finishedPayment: IFinishedPayment = sampleWithRequiredData;
        const finishedPaymentCollection: IFinishedPayment[] = [
          {
            ...finishedPayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFinishedPaymentToCollectionIfMissing(finishedPaymentCollection, finishedPayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinishedPayment to an array that doesn't contain it", () => {
        const finishedPayment: IFinishedPayment = sampleWithRequiredData;
        const finishedPaymentCollection: IFinishedPayment[] = [sampleWithPartialData];
        expectedResult = service.addFinishedPaymentToCollectionIfMissing(finishedPaymentCollection, finishedPayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finishedPayment);
      });

      it('should add only unique FinishedPayment to an array', () => {
        const finishedPaymentArray: IFinishedPayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const finishedPaymentCollection: IFinishedPayment[] = [sampleWithRequiredData];
        expectedResult = service.addFinishedPaymentToCollectionIfMissing(finishedPaymentCollection, ...finishedPaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const finishedPayment: IFinishedPayment = sampleWithRequiredData;
        const finishedPayment2: IFinishedPayment = sampleWithPartialData;
        expectedResult = service.addFinishedPaymentToCollectionIfMissing([], finishedPayment, finishedPayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(finishedPayment);
        expect(expectedResult).toContain(finishedPayment2);
      });

      it('should accept null and undefined values', () => {
        const finishedPayment: IFinishedPayment = sampleWithRequiredData;
        expectedResult = service.addFinishedPaymentToCollectionIfMissing([], null, finishedPayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(finishedPayment);
      });

      it('should return initial array if no FinishedPayment is added', () => {
        const finishedPaymentCollection: IFinishedPayment[] = [sampleWithRequiredData];
        expectedResult = service.addFinishedPaymentToCollectionIfMissing(finishedPaymentCollection, undefined, null);
        expect(expectedResult).toEqual(finishedPaymentCollection);
      });
    });

    describe('compareFinishedPayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFinishedPayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFinishedPayment(entity1, entity2);
        const compareResult2 = service.compareFinishedPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFinishedPayment(entity1, entity2);
        const compareResult2 = service.compareFinishedPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFinishedPayment(entity1, entity2);
        const compareResult2 = service.compareFinishedPayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
