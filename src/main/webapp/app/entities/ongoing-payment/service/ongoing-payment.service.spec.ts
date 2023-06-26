import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOngoingPayment } from '../ongoing-payment.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ongoing-payment.test-samples';

import { OngoingPaymentService } from './ongoing-payment.service';

const requireRestSample: IOngoingPayment = {
  ...sampleWithRequiredData,
};

describe('OngoingPayment Service', () => {
  let service: OngoingPaymentService;
  let httpMock: HttpTestingController;
  let expectedResult: IOngoingPayment | IOngoingPayment[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OngoingPaymentService);
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

    it('should create a OngoingPayment', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ongoingPayment = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ongoingPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OngoingPayment', () => {
      const ongoingPayment = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ongoingPayment).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OngoingPayment', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OngoingPayment', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OngoingPayment', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOngoingPaymentToCollectionIfMissing', () => {
      it('should add a OngoingPayment to an empty array', () => {
        const ongoingPayment: IOngoingPayment = sampleWithRequiredData;
        expectedResult = service.addOngoingPaymentToCollectionIfMissing([], ongoingPayment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ongoingPayment);
      });

      it('should not add a OngoingPayment to an array that contains it', () => {
        const ongoingPayment: IOngoingPayment = sampleWithRequiredData;
        const ongoingPaymentCollection: IOngoingPayment[] = [
          {
            ...ongoingPayment,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOngoingPaymentToCollectionIfMissing(ongoingPaymentCollection, ongoingPayment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OngoingPayment to an array that doesn't contain it", () => {
        const ongoingPayment: IOngoingPayment = sampleWithRequiredData;
        const ongoingPaymentCollection: IOngoingPayment[] = [sampleWithPartialData];
        expectedResult = service.addOngoingPaymentToCollectionIfMissing(ongoingPaymentCollection, ongoingPayment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ongoingPayment);
      });

      it('should add only unique OngoingPayment to an array', () => {
        const ongoingPaymentArray: IOngoingPayment[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ongoingPaymentCollection: IOngoingPayment[] = [sampleWithRequiredData];
        expectedResult = service.addOngoingPaymentToCollectionIfMissing(ongoingPaymentCollection, ...ongoingPaymentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ongoingPayment: IOngoingPayment = sampleWithRequiredData;
        const ongoingPayment2: IOngoingPayment = sampleWithPartialData;
        expectedResult = service.addOngoingPaymentToCollectionIfMissing([], ongoingPayment, ongoingPayment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ongoingPayment);
        expect(expectedResult).toContain(ongoingPayment2);
      });

      it('should accept null and undefined values', () => {
        const ongoingPayment: IOngoingPayment = sampleWithRequiredData;
        expectedResult = service.addOngoingPaymentToCollectionIfMissing([], null, ongoingPayment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ongoingPayment);
      });

      it('should return initial array if no OngoingPayment is added', () => {
        const ongoingPaymentCollection: IOngoingPayment[] = [sampleWithRequiredData];
        expectedResult = service.addOngoingPaymentToCollectionIfMissing(ongoingPaymentCollection, undefined, null);
        expect(expectedResult).toEqual(ongoingPaymentCollection);
      });
    });

    describe('compareOngoingPayment', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOngoingPayment(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOngoingPayment(entity1, entity2);
        const compareResult2 = service.compareOngoingPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOngoingPayment(entity1, entity2);
        const compareResult2 = service.compareOngoingPayment(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOngoingPayment(entity1, entity2);
        const compareResult2 = service.compareOngoingPayment(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
